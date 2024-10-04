package com.example.rides.service;

import com.example.rides.clients.DriverClient;
import com.example.rides.clients.PassengerClient;
import com.example.rides.dto.DriverResponseForRideDto;
import com.example.rides.dto.NominatimResponse;
import com.example.rides.dto.OSRMResponse;
import com.example.rides.dto.RideCreateResponseDto;
import com.example.rides.dto.RidesCreateDto;
import com.example.rides.dto.RidesInformationResponseDto;
import com.example.rides.kafka.KafkaProducer;
import com.example.rides.mapper.RidesMapper;
import com.example.rides.model.Rides;
import com.example.rides.model.enums.Status;
import com.example.rides.repository.RidesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class RidesService {

    private final static double PRICE_PER_METER = 2.;

    private final RidesRepository ridesRepository;

    private final RestClient restClient;

    private final DriverClient driverClient;

    private final PassengerClient passengerClient;

    private final RidesMapper ridesMapper;

    private final KafkaProducer kafkaProducer;

    private OSRMResponse calculateDistance(String startAddress, String endAddress) {

        String startCoordinates = geocodeAddress(encodeAddress(startAddress));
        String endCoordinates = geocodeAddress(encodeAddress(endAddress));

        String url = UriComponentsBuilder
                .fromUriString("http://router.project-osrm.org/route/v1/driving/{startCoordinates};{endCoordinates}")
                .queryParam("overview", "false")
                .build(startCoordinates, endCoordinates)
                .toString();

        OSRMResponse response = restClient.get().uri(url).retrieve().body(OSRMResponse.class);

        return response;
    }

    private String geocodeAddress(String address) {
        String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .queryParam("limit", "1")
                .build()
                .toString();

        NominatimResponse[] response = restClient.get().uri(url).retrieve().body(NominatimResponse[].class);

        return response[0].lon() + "," + response[0].lat();
    }

    private String encodeAddress(String address) {
        return address.replaceAll(" ", "+");
    }

    private double calcPrice(double distance, double pricePerMeter) {
        return distance * pricePerMeter;
    }

    @Transactional
    public RideCreateResponseDto addRide(RidesCreateDto ridesCreateDto) {
        Rides ride = ridesMapper.fromRidesCreateDto(ridesCreateDto);
        OSRMResponse osrmResponse = calculateDistance(ride.getStartPoint(), ride.getEndPoint());
        ride.setPrice(calcPrice(osrmResponse.routes().getFirst().distance(), PRICE_PER_METER));
        ridesRepository.save(ride);
        kafkaProducer.sendRideResponseToDriver(ridesMapper.toRideResponseForDriver(ride));
        return ridesMapper.toRideCreateResponseDto(ride, null, ride.getId());
    }

    public RidesInformationResponseDto checkPrice(String startPoint, String endPoint) {
        OSRMResponse osrmResponse = calculateDistance(startPoint, endPoint);
        RidesInformationResponseDto ridesInformationResponseDto = ridesMapper
                .toRidesInformationResponseDto(calcPrice(osrmResponse.routes().getFirst().distance(), PRICE_PER_METER),
                    osrmResponse
                            .routes()
                            .getFirst()
                            .distance(),
                    osrmResponse
                            .routes()
                            .getFirst()
                            .duration());
        return ridesInformationResponseDto;
    }

    @Transactional
    public void acceptRide(DriverResponseForRideDto driverResponseForRideDto, Long rideId) {

        Rides ride = ridesRepository.findById(rideId).orElseThrow(() -> new ResourceAccessException("Ride not found"));
        ride.setStatus(Status.ACCEPTED);
        ride.setDriverId(driverResponseForRideDto.id());
        kafkaProducer.notifyPassenger(
                ridesMapper
                        .toRideCreateResponseDto(ridesRepository.save(ride), driverResponseForRideDto, rideId));
    }
    //TODO change signature and logic
    public void declineRide(DriverResponseForRideDto driverResponseForRideDto, Long rideId) {
        Rides ride = ridesRepository.findById(rideId).orElseThrow(() -> new ResourceAccessException("Ride not found"));
        ride.setStatus(Status.DECLINED);
        kafkaProducer.notifyPassenger(ridesMapper
                .toRideCreateResponseDto(ridesRepository.save(ride), driverResponseForRideDto, rideId));
    }
    public void changeStatus(Status status, DriverResponseForRideDto driverResponseForRideDto,Long rideId) {
        Rides ride = ridesRepository.findById(rideId).orElseThrow(() -> new ResourceAccessException("Ride not found"));
        ride.setStatus(status);
        kafkaProducer.notifyPassenger(ridesMapper
                .toRideCreateResponseDto(ridesRepository.save(ride), driverResponseForRideDto, rideId));
    }

    public void endDrive(DriverResponseForRideDto driverResponseForRideDto, Long rideId) {
        Rides ride = ridesRepository.findById(rideId).orElseThrow(() -> new ResourceAccessException("Ride not found"));
        ride.setStatus(Status.COMPLETED);
        kafkaProducer.notifyDriver(ridesMapper.toRideResponseForDriver(ride));
        kafkaProducer.notifyPassenger(ridesMapper
                .toRideCreateResponseDto(ridesRepository.save(ride), driverResponseForRideDto, rideId));
    }
}