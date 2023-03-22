import React from 'react';
import Col from 'react-bootstrap/Col';
import Card from 'react-bootstrap/Card';

const ActualRepairWorks = (e) => {
    let items = e.works.map(function(item, key){
      return (
        <Col key={key} className="d-flex align-items-stretch">
          <Card className='rounded cardCustomBlue'>
          <Card.Img/>
          <Card.Body>
            <Card.Title>Work {key + 1} of {item.employee.employee.name}</Card.Title>
            <Card.Text>
              <li className="list-group-item active">Work Date: {item.date}</li>
              <li className="list-group-item">Working Hours: {item.workingHours}</li>
              <li className="list-group-item">Note: {item.note}</li>
            </Card.Text>
          </Card.Body>
        </Card>
        </Col>
      );
    });
    if(items.length === 0){
      return  (
      <div className="container text-center justify-content-center align-items-center">
        <header className="jumbotron rounded cardCustomRed">
          <h3>No recent works</h3>
        </header>
      </div>
      );
    }
    return items;
  }

  
export default ActualRepairWorks;