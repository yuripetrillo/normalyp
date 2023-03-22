package com.yuripe.normalizator.security.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuripe.normalizator.exceptions.CarException;
import com.yuripe.normalizator.models.Car;
import com.yuripe.normalizator.repositories.CarRepository;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return (List<Car>) carRepository.findAll();
    }

    public Car getCar(String plate) throws CarException {
        return carRepository.findByPlate(plate).orElseThrow(() -> new CarException("Car Not FOUND!"));
    }

    public void addCar(Car car) {
        this.carRepository.save(car);
    }

    public void updateCar(String id, Car car) {
        this.carRepository.save(car);
    }

    public void deleteCar(String plate) {
        this.carRepository.deleteByPlate(plate);;
    }
}
