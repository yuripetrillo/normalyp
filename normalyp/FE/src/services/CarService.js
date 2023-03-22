import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/car/';
const API_URL_US_GOV = 'https://www.fueleconomy.gov/ws/rest/vehicle/menu/';
//let user = JSON.parse(localStorage.getItem('user'));


class CarService {

  getCars() {
    return axios.get(API_URL + 'all', { headers: authHeader() });
  }

  addCar(Car, customerCF) {
    return axios.post(API_URL + 'addCar/' + customerCF, Car, { headers: authHeader() });
  }

  getCarByPlate(carPlate) {
    return axios.get(API_URL + 'getCar/' + carPlate, { headers: authHeader() });
  }

  updateCar(Car, CarId) {
    return axios.put(API_URL + 'updateCar/' + CarId , Car, { headers: authHeader() });
  }

  deleteCarById(CarId) {
    return axios.delete(API_URL + 'removeCar/' + CarId, { headers: authHeader() });
  }

  //TODO: spostare chiamata API esterna su BE per gestire eventuali errori
  getModelYears() {
    return axios.get(API_URL_US_GOV + 'year');
  }

  getMakesByYear(year) {
    return axios.get(API_URL_US_GOV + 'make?year=' + year);
  }

  getModelsByYearAndMake(year, make) {
    return axios.get(API_URL_US_GOV + 'model?year=' + year + "&make=" + make);
  }
}

export default new CarService();