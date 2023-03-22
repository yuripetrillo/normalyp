import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/verify/';
let user = JSON.parse(localStorage.getItem('user'));


class UserService {
  getPublicContent() {
    return axios.get(API_URL + 'all');
  }

  getUserBoard() {
    //console.log(getCarInfo()); // Check
    return axios.get(API_URL + 'employee', {
      headers: {
        Authorization: 'Bearer ' + user.accessToken,
        Username : user.username
      }});
  }

  getModeratorBoard() {
    return axios.get(API_URL + 'supervisor', { headers: authHeader() });
  }

  getAdminBoard() {
    return axios.get(API_URL + 'admin', { headers: authHeader() });
  }
}

export default new UserService();
