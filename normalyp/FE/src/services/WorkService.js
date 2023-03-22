import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/work/';
//let user = JSON.parse(localStorage.getItem('user'));


class WorkService {

  getWorks() {
    return axios.get(API_URL + 'all', { headers: authHeader() });
  }

  addWork(work, employeeWorking, repairId) {
    return axios.post(API_URL + 'add/' + employeeWorking + "/" + repairId, work, { headers: authHeader() });
  }

  getWorkById(workId) {
    return axios.get(API_URL + 'getWork/' + workId, { headers: authHeader() });
  }

  getWorkByEmployeeId(employeeId) {
    return axios.get(API_URL + 'getWorkEmployee/' + employeeId, { headers: authHeader() });
  }

  getWorkByRepairId(repairId) {
    return axios.get(API_URL + 'getWorkRepair/' + repairId, { headers: authHeader() });
  }

  updateWork(work, workId) {
    return axios.put(API_URL + 'updateWork/' + workId , work, { headers: authHeader() });
  }

  deleteWorkById(workId) {
    return axios.delete(API_URL + 'removeWork/' + workId, { headers: authHeader() });
  }
}

export default new WorkService();
