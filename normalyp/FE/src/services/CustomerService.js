import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/customer/';
let user = JSON.parse(localStorage.getItem('user'));


class CustomerService {

  getCustomers() {
    return axios.get(API_URL + 'all', { headers: authHeader() });
  }

  addCustomer(Customer) {
    return axios.post(API_URL + 'addCustomer', Customer, { headers: authHeader() });
  }

  getCustomerByCF(CustomerCF) {
    return axios.get(API_URL + 'getCustomer/' + CustomerCF, { headers: authHeader() });
  }

  updateCustomer(Customer, CustomerId) {
    return axios.put(API_URL + 'updateCustomer/' + CustomerId , Customer, { headers: authHeader() });
  }

  deleteCustomerById(CustomerId) {
    return axios.delete(API_URL + 'removeCustomer/' + CustomerId, { headers: authHeader() });
  }
}

export default new CustomerService();
