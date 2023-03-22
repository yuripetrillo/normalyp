import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/repair/';
let user = JSON.parse(localStorage.getItem('user'));


class RepairService {

  getRepairs() {
    return axios.get(API_URL + 'all', { headers: authHeader() });
  }

  getRepairsNotDone() {
    return axios.get(API_URL + 'all/notDone', { headers: authHeader() });
  }

  getRepairsDone() {
    return axios.get(API_URL + 'all/done', { headers: authHeader() });
  }

  addRepair(repair, employeeWorking) {
    return axios.post(API_URL + 'add/' + employeeWorking, repair, { headers: authHeader() });
  }

  getRepairById(repairId) {
    return axios.get(API_URL + 'getRepair/' + repairId, { headers: authHeader() });
  }

  getRepairByEmployeeId(employeeId) {
    return axios.get(API_URL + 'getRepairEmployee/' + employeeId, { headers: authHeader() });
  }

  getRepairToDoByEmployeeId(employeeId) {
    return axios.get(API_URL + 'getRepairToDoEmployee/' + employeeId, { headers: authHeader() });
  }

  updateRepair(repair, repairId) {
    return axios.put(API_URL + 'updateRepair/' + repairId , repair, { headers: authHeader() });
  }

  deleteRepairById(repairId) {
    return axios.delete(API_URL + 'removeRepair/' + repairId, { headers: authHeader() });
  }

  closeRepairById(repairId) {
    return axios.post(API_URL + 'closeRepair/' + repairId, null, { headers: authHeader() });
  }

  takeRepairById(repairId) {
    return axios.post(API_URL + 'takeRepair/' + repairId, null, { headers: authHeader() });
  }

}

export default new RepairService();
