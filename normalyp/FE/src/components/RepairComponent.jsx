import React, { Component } from "react";
import RepairService from "../services/RepairService";
import CustomerService from "../services/CustomerService";
import CarService from "../services/CarService";
import RedirectButton from "./redirectButton";
import Input from "react-validation/build/input";
import Form from "react-validation/build/form";
import CustomerComponent from "./CustomerComponent";
import CarComponent from "./CarComponent";
import DatePicker from "react-datepicker";
import CheckButton from "react-validation/build/button";
import Select from "react-select";
import EmployeeService from "../services/EmployeeService";

const required = (value) => {
  if (!value || value === "") {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

const vcarPlate = (value) => {
  if (value.length < 5 || value.length > 10) {
    return (
      <div className="alert alert-danger" role="alert">
        Car Plate must be between 5 and 10 characters.
      </div>
    );
  }
};

const vemployeeWorking = (value) => {
  if (value.length < 3 || value.length > 20) {
    return (
      <div className="alert alert-danger" role="alert">
        Employee Name must be between 3 and 20 characters.
      </div>
    );
  }
};

export default class RepairComponent extends Component {
  constructor(props) {
    super(props);

    var url = window.location.pathname;
    var id = url.substring(url.lastIndexOf("/") + 1);
    this.state = {
      id: id,
      customerCF: "",
      carPlate: "",
      employeeWorking: "",
      repairType: "",
      repairHours: "",
      partsTotalPrice: "",
      changeExternalCostsHandler: "",
      externalTotalPrice: "",
      workTotalPrice: "",
      startDate: "",
      endDate: "",
      addedCF: "",
      customerExist: false,
      carExist: false,
      canUpdateCarPlate: true,
      check: false,
      addCustomer: false,
      addCar: false,
      done: "",
      hideValidation: false,
      customerAddedSuccessfully: "",
      carAddedSuccessfully: "",
      employees: [],
    };
    this.changeCustomerCFHandler = this.changeCustomerCFHandler.bind(this);
    this.changeCarPlateHandler = this.changeCarPlateHandler.bind(this);
    this.changeEmployeeWorkingHandler = this.changeEmployeeWorkingHandler.bind(this);
    this.changeRepairTypeHandler = this.changeRepairTypeHandler.bind(this);
    this.changeRepairHoursHandler = this.changeRepairHoursHandler.bind(this);
    this.changePartsCostsHandler = this.changePartsCostsHandler.bind(this);
    this.changeExternalCostsHandler = this.changeExternalCostsHandler.bind(this);
    this.changeTotalPriceHandler = this.changeTotalPriceHandler.bind(this);
    this.changeStartDateHandler = this.changeStartDateHandler.bind(this);
    this.changeEndDateHandler = this.changeEndDateHandler.bind(this);
    this.changeIsDoneHandler = this.changeIsDoneHandler.bind(this);
  }

  componentDidMount() {
    EmployeeService.getEmployees().then(
      (response) => {
        const employeeDDList = [{ text: "", value: "" }];
        response.data
          .map((object) => object.name)
          .forEach((e, i) => (employeeDDList[i] = { text: e, value: e }));
        this.setState({ employees: employeeDDList });
      },
      (error) => {
        const resMessage =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();
        this.setState({
          successful: false,
          message: resMessage + " getEmployees KO",
        });
      }
    );
    if (this.state.id === "_add") {
      return;
    } else {
      RepairService.getRepairById(this.state.id).then(
        (response) => {
          let repair = response.data;
          this.setState({
            customerCF: repair.customerCF,
            carPlate: repair.carPlate,
            done: repair.done,
            employeeWorking: repair.employee.name,
            repairType: repair.repairType,
            repairHours: repair.repairHours,
            partsTotalPrice: repair.partsTotalPrice,
            externalTotalPrice: repair.externalTotalPrice,
            workTotalPrice: repair.workTotalPrice,
            startDate: repair.startDate,
            endDate: repair.endDate,
          });
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
          this.setState({
            successful: false,
            message: resMessage + " getRepairs KO",
          });
        }
      );
    }
  }

  customerCallbackFunction = (childData) => {
    console.log(childData);
    console.log("Validation: " + this.state.hideValidation);
    console.log("Car: " + this.state.carExist);

    this.setState({
      customerAddedSuccessfully: childData,
      customerExist: childData === "error" ? false : true,
      hideValidation: childData === "error" ? true : false,
    });
  };

  carCallbackFunction = (childData) => {
    this.setState({
      carAddedSuccessfully: childData,
      carExist: childData === "error" ? false : true,
      hideValidation: childData === "error" ? true : false,
    });
  };

  saveOrEditRepair = (e) => {
    e.preventDefault();
    this.form.validateAll();
    if (this.checkBtn.context._errors.length === 0) {
      let repair = {
        employee: { name: this.state.employeeWorking },
        customerCF: this.state.customerCF,
        repairType: this.state.repairType,
        repairHours: this.state.repairHours,
        carPlate: this.state.carPlate,
        startDate: this.state.startDate,
        endDate: this.state.endDate,
        partsTotalPrice: this.state.partsTotalPrice,
        externalTotalPrice: this.state.externalTotalPrice,
        workTotalPrice: this.state.workTotalPrice,
        done: this.state.done,
      };
      console.log(repair);
      if (this.state.id === "_add") {
        RepairService.addRepair(repair, repair.employee.name).then(
          (response) => {
            this.setState({
              message: Object.values(response.data),
              successful: true,
            });
          },
          (error) => {
            const resMessage =
              (error.response &&
                error.response.data &&
                error.response.data.message) ||
              error.message ||
              error.toString();
            this.setState({
              successful: false,
              message: resMessage + " KO",
            });
          }
        );
      } else {
        RepairService.updateRepair(repair, this.state.id).then(
          (response) => {
            //Add redirect to /admin
            this.setState({
              message: response.data,
              successful: true,
            });
          },
          (error) => {
            const resMessage =
              (error.response &&
                error.response.data &&
                error.response.data.message) ||
              error.message ||
              error.toString();
            this.setState({
              successful: false,
              message: resMessage + " KO",
            });
          }
        );
      }
    }
  };

  changeCustomerCFHandler = (event) => {
    this.setState({ customerCF: event.target.value });
  };

  changeCarPlateHandler = (event) => {
    this.setState({ carPlate: event.target.value });
  };

  changeEmployeeWorkingHandler = (event) => {
    this.setState({ employeeWorking: event.target.value });
  };

  changeRepairTypeHandler = (event) => {
    this.setState({ repairType: event.target.innerHTML });
  };

  changeRepairHoursHandler = (event) => {
    this.setState({ repairHours: event.target.value });
  };

  changePartsCostsHandler = (event) => {
    this.setState({ partsTotalPrice: event.target.value });
  };

  changeExternalCostsHandler = (event) => {
    this.setState({ externalTotalPrice: event.target.value });
  };

  changeTotalPriceHandler = (event) => {
    this.setState({ workTotalPrice: event.target.value });
  };

  changeStartDateHandler = (event) => {
    this.setState({ startDate: event.toLocaleDateString("en-CA") });
  };

  changeEndDateHandler = (event) => {
    this.setState({ endDate: event.toLocaleDateString("en-CA") });
  };

  changeIsDoneHandler = (event) => {
    this.setState({ done: event.target.checked });
  };

  handleChangeValue = (e) => console.log("ECCOMI " + e.target.value); //this.setState({addedCF: e.target.value});

  getTitle() {
    if (this.state.id === "_add")
      return <h2 className="text-center">Add Repair</h2>;
    else {
      return <h2 className="text-center">Update Repair</h2>;
    }
  }

  /*
    <Input placeholder='Customer CF' name='customerCF' className='form-control' value={this.state.customerCF} onChange={this.changeCustomerCFHandler} validations={[required, vcarMake]}/>
    */

  checkCustomer = (e) => {
    e.preventDefault();
    CustomerService.getCustomerByCF(this.state.customerCF).then(
      (response) => {
        this.setState({
          customerExist: true,
          customerCF: response.data.cf,
          messageCheck: "Customer " + response.data.cf + " exist",
          successful: true,
        });
      },
      (error) => {
        const resMessage =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();
        this.setState({
          successful: false,
          messageCheck: resMessage,
        });
      }
    );
    this.timer();
  };

  checkCar = (e) => {
    e.preventDefault();
    CarService.getCarByPlate(this.state.carPlate).then(
      (response) => {
        this.setState({
          carPlate: response.data.plate,
          messageCheck: "Car " + response.data.plate + " exist",
          successful: true,
          canUpdateCarPlate: false,
        });
        setTimeout(() => {
          this.setState({ carExist: true });
        }, 2000);
      },
      (error) => {
        const resMessage =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();
        this.setState({
          successful: false,
          messageCheck: resMessage,
        });
      }
    );
    this.timer();
  };

  timer = () => {
    this.setState({ check: !this.state.check });
    setTimeout(() => {
      this.setState({ check: !this.state.check });
    }, 2000);
  };

  vcf = (value) => {
    return value.length < 16 || value.length > 16 ? false : true;
  };

  addRepairValid() {
    this.setState({ customerExist: !this.state.customerExist });
  }

  addNewCustomer = (e) => {
    e.preventDefault();
    this.setState({ addCustomer: true, hideValidation: true });
  };

  addNewCar = (e) => {
    e.preventDefault();
    this.setState({ addCar: true, hideValidation: true });
  };

  onChangeDDEmployee = (item, name) => {
    this.setState({ employee: item.value, employeeWorking: item.value });
  };

  render() {
    return (
      <div>
        {this.state.addCustomer &&
        this.state.customerAddedSuccessfully !== "success" ? (
          <CustomerComponent
            value={this.state.customerCF}
            parentCallback={this.customerCallbackFunction}
            onChangeValue={this.handleAddedCFValue}
          />
        ) : null}
        {this.state.addCar && this.state.carAddedSuccessfully !== "success" ? (
          <CarComponent
            value={this.state.carPlate}
            customerCF={this.state.customerCF}
            parentCallback={this.carCallbackFunction}
            onChangeValue={this.handleAddedCarPlateValue}
          />
        ) : null}
        {this.state.id === "_add" ? (
          <div
            className="container"
            hidden={
              this.state.carExist || this.state.hideValidation ? "hidden" : ""
            }
          >
            <div
              id="repairValidation"
              className="h-100 d-flex align-items-center justify-content-center"
            >
              <div className="cardCustom col-md-6 offset-md-3 offset-md-3">
                <div className="card-body">
                  <h2 className="text-center">New Repair - Requirements</h2>
                  <div className="form-group">
                    <div className="text-center justify-content-center align-items-center">
                      <label>Customer Fiscal Code</label>
                      <input
                        disabled={
                          this.state.customerExist ||
                          this.state.customerAddedSuccessfully === "success"
                            ? "disabled"
                            : ""
                        }
                        placeholder="Customer CF"
                        name="customerCF"
                        className="form-control"
                        value={this.state.customerCF}
                        onChange={this.changeCustomerCFHandler}
                        validations={[required]}
                      />
                      <br></br>
                      <button
                        disabled={
                          this.state.customerExist ||
                          this.state.customerCF === "" ||
                          !this.vcf(this.state.customerCF) ||
                          this.state.customerAddedSuccessfully === "success"
                            ? "disabled"
                            : ""
                        }
                        type="button"
                        onClick={this.checkCustomer}
                        value={this.state.customerCF}
                        className="btn btn-primary"
                      >
                        Check Customer
                      </button>
                    </div>
                  </div>
                  <div className="form-group">
                    <div className="text-center justify-content-center align-items-center">
                      <label>Car Plate</label>
                      <input
                        disabled={
                          !this.state.canUpdateCarPlate ||
                          !this.state.customerExist ||
                          this.state.carExist
                            ? "disabled"
                            : ""
                        }
                        placeholder="Car Plate"
                        name="carPlate"
                        className="form-control"
                        value={this.state.carPlate}
                        onChange={this.changeCarPlateHandler}
                        validations={[required, vcarPlate]}
                      />
                      <br></br>
                      <button
                        disabled={
                          !this.state.canUpdateCarPlate ? "disabled" : ""
                        }
                        hidden={!this.state.customerExist ? "hidden" : ""}
                        type="button"
                        className="btn btn-primary"
                        onClick={this.checkCar}
                      >
                        Check Car
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        ) : null}
        {this.state.check ? (
          <div className="container">
            <div className="row align-items-center justify-content-center">
              <div
                className={
                  this.state.successful
                    ? "alert alert-success"
                    : "alert alert-danger"
                }
                role="alert"
              >
                {this.state.messageCheck}
              </div>
            </div>
            <div className="row align-items-center justify-content-center">
              <button
                hidden={
                  this.state.successful || this.state.customerExist
                    ? "hidden"
                    : ""
                }
                type="button"
                className="btn btn-success"
                onClick={this.addNewCustomer}
              >
                Add new Customer
              </button>
              <button
                hidden={
                  this.state.successful || !this.state.customerExist
                    ? "hidden"
                    : ""
                }
                type="button"
                className="btn btn-success"
                onClick={this.addNewCar}
              >
                Add new Car
              </button>
            </div>
          </div>
        ) : null}
        <div
          className="container align-items-center justify-content-center"
          hidden={
            !this.state.carExist && this.state.id === "_add" ? "hidden" : ""
          }
        >
          <Form
            className="newElementForm align-items-center justify-content-center"
            onSubmit={this.saveOrEditRepair}
            ref={(c) => {
              this.form = c;
            }}
            autoComplete="off"
          >
            <div className="card">{this.getTitle()}</div>
            <div
              className="container align-items-center justify-content-center"
              hidden={this.state.id !== "_add" ? "hidden" : ""}
            >
              <div className="row align-items-center justify-content-center">
                <label>Customer Fiscal Code</label>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="alert alert-success">
                  <p className="font-weight-bold shadow-none p-1 mb-1 bg-light rounded">
                    {" "}
                    {this.state.customerCF}{" "}
                  </p>
                </div>
              </div>
            </div>
            <div
              className="container"
              hidden={this.state.id !== "_add" ? "hidden" : ""}
            >
              <div className="row align-items-center justify-content-center">
                <label>Car Plate</label>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="alert alert-success">
                  <p className="font-weight-bold shadow-none p-1 mb-1 bg-light rounded">
                    {" "}
                    {this.state.carPlate}{" "}
                  </p>
                </div>
              </div>
            </div>
            <div className="container align-items-center justify-content-center">
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label>Repair Type</label>
                  <div className="btn-group dropright">
                    <button
                      type="button"
                      className="btn btn-secondary dropdown-toggle"
                      data-toggle="dropdown"
                      aria-haspopup="true"
                      aria-expanded="false"
                      name="repairType"
                    >
                      Repair Type
                    </button>
                    <div className="dropdown-menu">
                      <button
                        className="dropdown-item"
                        type="button"
                        value={this.state.repairType}
                        onClick={this.changeRepairTypeHandler}
                      >
                        Painting
                      </button>
                      <button
                        className="dropdown-item"
                        type="button"
                        value={this.state.repairType}
                        onClick={this.changeRepairTypeHandler}
                      >
                        Painting and body repair
                      </button>
                    </div>
                  </div>
                  <div className="float-right">
                    <Input
                      placeholder="Repair Type"
                      name="repairType"
                      className={
                        this.state.id !== "_add"
                          ? "text-center form-control alert-primary"
                          : "form-control"
                      }
                      value={this.state.repairType}
                      validations={[required]}
                      disabled
                      hidden={this.state.repairType.length > 1 ? "" : "hidden"}
                    />
                  </div>
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label>Total expected Repair Hours</label>
                  <Input
                    required
                    type="number"
                    min="1"
                    placeholder="Repair Hours"
                    name="repairHours"
                    className={
                      this.state.id !== "_add"
                        ? "form-control alert-primary"
                        : "form-control"
                    }
                    value={this.state.repairHours}
                    onChange={this.changeRepairHoursHandler}
                    validations={[required]}
                  />
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label>Parts costs</label>
                  <Input
                    required
                    type="number"
                    min="1"
                    placeholder="Parts costs"
                    name="partsTotalPrice"
                    className={
                      this.state.id !== "_add"
                        ? "form-control alert-primary"
                        : "form-control"
                    }
                    value={this.state.partsTotalPrice}
                    onChange={this.changePartsCostsHandler}
                    validations={[required]}
                  />
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label>External costs</label>
                  <Input
                    required
                    type="number"
                    min="1"
                    placeholder="External costs"
                    name="externalTotalPrice"
                    className={
                      this.state.id !== "_add"
                        ? "form-control alert-primary"
                        : "form-control"
                    }
                    value={this.state.externalTotalPrice}
                    onChange={this.changeExternalCostsHandler}
                    validations={[required]}
                  />
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label>Total price</label>
                  <Input
                    required
                    type="number"
                    min="1"
                    placeholder="Total price"
                    name="workTotalPrice"
                    className={
                      this.state.id !== "_add"
                        ? "form-control alert-primary"
                        : "form-control"
                    }
                    value={this.state.workTotalPrice}
                    onChange={this.changeTotalPriceHandler}
                    validations={[required]}
                  />
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label>Start Date</label>
                  <DatePicker
                    placeholderText={"Please select a date"}
                    className="alert-primary text-center"
                    name="startDate"
                    value={this.state.startDate}
                    validations={[required]}
                    onChange={this.changeStartDateHandler}
                  ></DatePicker>
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label>End Date</label>
                  <DatePicker
                    placeholderText={"Please select a date"}
                    className="alert-primary text-center"
                    name="endDate"
                    value={this.state.endDate}
                    validations={[required]}
                    onChange={this.changeEndDateHandler}
                  ></DatePicker>
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label>Employee Name</label>
                  <Select
                    getOptionLabel={(option) => option.text}
                    getOptionValue={(option) => option.value}
                    options={this.state.employees}
                    placeholder="Select Employee..."
                    onChange={this.onChangeDDEmployee}
                    isSearchable={true}
                    validations={[required, vemployeeWorking]}
                  />
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <label className="toggle">
                    <input
                      className="toggle-checkbox"
                      type="checkbox"
                      onChange={this.changeIsDoneHandler}
                    />
                    <div className="toggle-switch"></div>
                    <span className="toggle-label">Is this repair Done?</span>
                  </label>
                </div>
              </div>
              <div className="row align-items-center justify-content-center">
                <div className="form-group col-md-6 align-items-center justify-content-center">
                  <button type="submit" className="btn btn-success">
                    Save
                  </button>
                  <RedirectButton
                    goTo="/repairs"
                    className="btn btn-danger"
                    text="Cancel"
                  />
                  {this.state.message && (
                    <div className="row align-items-center justify-content-center">
                      <div
                        className={
                          this.state.successful
                            ? "alert alert-success"
                            : "alert alert-danger"
                        }
                        role="alert"
                      >
                        {this.state.message}
                      </div>
                    </div>
                  )}
                  <CheckButton
                    style={{ display: "none" }}
                    ref={(c) => {
                      this.checkBtn = c;
                    }}
                  />
                </div>
              </div>
            </div>
          </Form>
        </div>
      </div>
    );
  }
}
