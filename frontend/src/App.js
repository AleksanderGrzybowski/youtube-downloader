import React, { Component } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import LinkStep from './LinkStep';
import MediaTypeStep from './MediaTypeStep';
import WaitStep from './WaitStep';
import DownloadStep from './DownloadStep';

class App extends Component {

    constructor(props) {
        super(props);

        // noinspection JSUnusedGlobalSymbols
        this.state = {
            movieLink: '',
            mediaType: 'video',
            requestInProgress: false,
            step: 1,
            downloadLink: ''
        }
    }

    onMovieLinkChange = movieLink => this.setState({movieLink});

    onMediaTypeChange = mediaType => this.setState({mediaType});

    render() {
        let view;

        if (this.state.step === 1) {
            view = (
              <LinkStep
                movieLink={this.props.movieLink}
                onMovieLinkChange={this.onMovieLinkChange}
                requestInProgress={this.state.requestInProgress}
                onNext={() => this.setState({step: 2})}
              />
            )
        } else if (this.state.step === 2) {
            view = (
              <MediaTypeStep
                mediaType={this.state.mediaType}
                onMediaTypeChange={this.onMediaTypeChange}
                onNext={() => this.setState({step: 3})}
              />
            )
        } else if (this.state.step === 3) {
            view = (
              <WaitStep/>
            )
        } else if (this.state.step === 4) {
            view = (
              <DownloadStep
                downloadLink={this.state.downloadLink}
              />
            )
        }

        return (
          <Grid>
              <Row>
                  <Col md={12}>
                      <h1 className="text-center">ytdownloader</h1>
                  </Col>
              </Row>
              <Row>
                  <Col md={12}>
                      {view}
                  </Col>
              </Row>
          </Grid>
        );
    }
}

export default App;
