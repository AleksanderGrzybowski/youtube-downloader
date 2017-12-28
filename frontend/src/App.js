import React, { Component } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.css';

class App extends Component {
    render() {
        return (
          <Grid>
              <Row>
                  <Col><h1>Bootstrap OK</h1></Col>
              </Row>
          </Grid>
        );
    }
}

export default App;
