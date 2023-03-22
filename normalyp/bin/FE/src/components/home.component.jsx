import React, { Component } from "react";
import image1 from '../images/presentation/1.jpg';
import image2 from '../images/presentation/2.jpg';
import image3 from '../images/presentation/3.jpg';
import UserService from "../services/user.service";

export default class Home extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content: ""
    };
  }

  componentDidMount() {
    UserService.getPublicContent().then(
      response => {
        this.setState({
          content: response.data
        });
      },
      error => {
        this.setState({
          content:
            (error.response && error.response.data) ||
            error.message ||
            error.toString()
        });
      }
    );
  }

  render() {
    return (
      <div className="container text-center justify-content-center align-items-center">
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossOrigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossOrigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossOrigin="anonymous"></script>
        <header className="jumbotron rounded cardCustom">
          <h3>Company Home Page</h3>
        </header>
        <div id="carousel" className="carousel slide" data-ride="carousel">
          <ol className="carousel-indicators">
              <li data-target="#carousel" data-slide-to="0" className="active"></li>
              <li data-target="#carousel" data-slide-to="1"></li>
              <li data-target="#carousel" data-slide-to="2"></li>
          </ol>
          <div className="carousel-inner">
              <div className="carousel-item active">
                  <img className="d-block w-100" src={image1} alt="First slide"/>
                  <div className="carousel-caption d-none d-md-block">
                      <h5>My Title (1st Image)</h5>
                      <p>The whole caption will be written when ready.</p>
                  </div>
              </div>
              <div className="carousel-item">
                  <img className="d-block w-100" src={image2} alt="Second slide"/>
                  <div className="carousel-caption d-none d-md-block">
                      <h5>My Title (2nd Image)</h5>
                      <p>The whole caption will be written when ready.</p>
                  </div>
              </div>
              <div className="carousel-item">
                  <img className="d-block w-100" src={image3} alt="Third slide"/>
                  <div className="carousel-caption d-none d-md-block">
                      <h5>My Title (3rd Image)</h5>
                      <p>The whole caption will be written when ready.</p>
                  </div>
              </div>
          </div>
          <a className="carousel-control-prev" href="#carousel" role="button" data-slide="prev">
              <span className="carousel-control-prev-icon" aria-hidden="true"></span>
              <span className="sr-only">Previous</span>
          </a>
          <a className="carousel-control-next" href="#carousel" role="button" data-slide="next">
              <span className="carousel-control-next-icon" aria-hidden="true"></span>
              <span className="sr-only">Next</span>
          </a>
        </div>
        </div>
    );
  }
}
