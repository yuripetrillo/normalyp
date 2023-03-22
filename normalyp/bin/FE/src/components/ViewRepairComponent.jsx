import React, { Component } from "react";
import RepairService from "../services/RepairService";
import WorkService from "../services/WorkService";
import RedirectButton from "./redirectButton";
import ActualRepairWorks from "./ActualRepairWorks";
import Row from "react-bootstrap/Row";
import Container from "react-bootstrap/Container";

class ViewRepairComponent extends Component {
  constructor(props) {
    super(props);

    var url = window.location.pathname;
    var id = url.substring(url.lastIndexOf("/") + 1);
    this.state = {
      id: id,
      repair: [],
      works: [],
    };
  }

  componentDidMount() {
    RepairService.getRepairById(this.state.id).then(
      (response) => {
        this.setState({ repair: response.data, car: response.data.car });
      },
      (error) => {
        typeof error.response.data !== "undefined"
          ? this.setState({
              content: error.response.data.message.toString(),
            })
          : this.setState({
              content: "Cannot retreive data from BackEnd!",
            });
      }
    );
    if (this.state.id === "_add") {
      return;
    } else {
      WorkService.getWorkByRepairId(this.state.id).then(
        (response) => {
          let works = response.data;
          this.setState({ works: works });
        },
        (error) => {
          typeof error.response.data !== "undefined"
            ? this.setState({
                content: error.response.data.message.toString(),
              })
            : this.setState({
                content: "Cannot retreive data from BackEnd!",
              });
        }
      );
    }
  }

  /*getValueObject = (obj, key) => {
        return Object.keys(obj).filter(objectToFind => objectToFind === key).map(foundObj => (obj[foundObj]))[0];
      }*/
  render() {
    console.log(this.state.works)
    return (
      <div>
        <div className="cardCustomGreen card col-md-6 offset-md-3 container text-center justify-content-center align-items-center">
          <h3 className="text-center">View Repair Details</h3>
          <div className="alert cardCustom">
            <div className="row text-center justify-content-center align-items-center">
              <span className="">Repair Id: </span>
              <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded">
                {" "}
                {this.state.repair.repairId}{" "}
              </p>
            </div>
            <div className="row text-center justify-content-center align-items-center">
              <span className="">Repair Type: </span>
              <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded">
                {" "}
                {this.state.repair.repairType}{" "}
              </p>
            </div>
            <div className="row text-center justify-content-center align-items-center">
              <span className="">Car Make: </span>
              <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded">
                {" "}
                {Object.keys(this.state.repair)
                  .filter((objectToFind) => objectToFind === "car")
                  .map((foundObj) => this.state.repair[foundObj].make)}
              </p>
            </div>
            <div className="row text-center justify-content-center align-items-center">
              <span className="">Car Model: </span>
              <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded">
                {" "}
                {Object.keys(this.state.repair)
                  .filter((objectToFind) => objectToFind === "car")
                  .map((foundObj) => this.state.repair[foundObj].model)}{" "}
              </p>
            </div>
            <div className="row text-center justify-content-center align-items-center">
              <span className="">Employee: </span>
              <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded">
                {" "}
                {Object.keys(this.state.repair)
                  .filter((objectToFind) => objectToFind === "employee")
                  .map((foundObj) => this.state.repair[foundObj].name)}{" "}
              </p>
            </div>
          </div>
          <RedirectButton
            goTo="/repairs"
            className="btn btn-outline-dark"
            text="Back to Repairs"
          />
          <RedirectButton
            goTo="/works/manageWork"
            values={"close/" + this.state.id}
            className="btn btn-outline-danger"
            text="Close This Repair"
          />
        </div>
        <Container
          className="text-center justify-content-center align-items-center"
          fluid
        >
          <Row>
            {this.state.works.length >= 1 ? (
              <ActualRepairWorks works={this.state.works}></ActualRepairWorks>
            ) : (/*Gestire chiamata async e attendere solo nel caso si stiano aspettando risultati dall'API*/
              <div className="container text-center justify-content-center align-items-center">
                <header className="jumbotron rounded cardCustomYellow">
                  <h3>Works loading...</h3>
                </header>
              </div>
            )}
          </Row>
        </Container>
      </div>
    );
  }
}

export default ViewRepairComponent;
