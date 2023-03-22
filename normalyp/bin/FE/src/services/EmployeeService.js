import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/employee/';
let user = JSON.parse(localStorage.getItem('user'));


class EmployeeService {

  getEmployees() {
    return axios.get(API_URL + 'all', { headers: authHeader() });
  }

  addEmployees(employee) {
    return axios.post(API_URL + 'addEmployee', employee, { headers: authHeader() });
  }

  getEmployeeById(employeeId) {
    return axios.get(API_URL + 'getEmployee/' + employeeId, { headers: authHeader() });
  }

  updateEmployee(employee, employeeId) {
    return axios.put(API_URL + 'updateEmployee/' + employeeId , employee, { headers: authHeader() });
  }

  deleteEmployeeById(employeeId) {
    return axios.delete(API_URL + 'removeEmployee/' + employeeId, { headers: authHeader() });
  }
}

export default new EmployeeService();
