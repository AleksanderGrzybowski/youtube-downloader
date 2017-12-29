import React, { Component } from 'react';
import { Alert, Col, Grid, Row } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import LinkStep from './LinkStep';
import MediaTypeStep from './MediaTypeStep';
import WaitStep from './WaitStep';
import DownloadStep from './DownloadStep';
import { createDownloadJob, getDownloadJob, getThumbnailLink } from './backend';
import 'font-awesome-webpack';

class App extends Component {

    constructor(props) {
        super(props);

        // noinspection JSUnusedGlobalSymbols
        this.state = {
            movieLink: '',
            thumbnailLink: '',
            mediaType: 'video',
            requestInProgress: false,
            jobId: 0,
            step: 1,
            downloadLink: '',
            errorMessage: ''
        }
    }

    onMovieLinkChange = movieLink => this.setState({movieLink});

    onMediaTypeChange = mediaType => this.setState({mediaType});

    startRequest = () => this.setState({requestInProgress: true, errorMessage: ''});

    fetchThumbnailLink = () => {
        this.startRequest();

        getThumbnailLink()
          .then(thumbnailLink => this.setState({requestInProgress: false, thumbnailLink, step: 2}))
          .catch(e => {
              this.setState({requestInProgress: false, errorMessage: 'An error occured when checking URL. Is it correct?'});
              console.error(e);
          });
    };

    startPolling = () => {
        this.startRequest();

        const timerId = setInterval(() => {
            getDownloadJob(this.state.jobId)
              .then(job => {
                  if (job.status === 'SUCCESS') {
                      clearInterval(timerId);
                      this.setState({step: 4, requestInProgress: false, downloadLink: 'TODO build download link from job id'})
                  } else if (job.status === 'ERROR') {
                      clearInterval(timerId);
                      this.setState({requestInProgress: false, errorMessage: 'An error occured while downloading. Please try again.'});
                  }
              })
              .catch(e => {
                  clearInterval(timerId);
                  this.setState({requestInProgress: false, errorMessage: 'An error occured while downloading. Please try again.'});
                  console.error(e);
              });
        }, 2000);
    };

    createJobAndStartPolling = () => {
        this.startRequest();

        createDownloadJob(this.state.movieLink)
          .then(job => {
              this.setState({requestInProgress: true, jobId: job.id, step: 3});
              this.startPolling();
          })
          .catch(e => {
              this.setState({requestInProgress: false, errorMessage: 'An error occured while downloading. Please try again.'});
              console.error(e);
          });
    };


    render() {
        let view;

        if (this.state.step === 1) {
            view = (
              <LinkStep
                movieLink={this.state.movieLink}
                onMovieLinkChange={this.onMovieLinkChange}
                requestInProgress={this.state.requestInProgress}
                onNext={this.fetchThumbnailLink}
              />
            )
        } else if (this.state.step === 2) {
            view = (
              <MediaTypeStep
                thumbnailLink={this.state.thumbnailLink}
                mediaType={this.state.mediaType}
                onMediaTypeChange={this.onMediaTypeChange}
                onNext={this.createJobAndStartPolling}
                requestInProgress={this.state.requestInProgress}
              />
            )
        } else if (this.state.step === 3) {
            view = <WaitStep/>;
        } else if (this.state.step === 4) {
            view = <DownloadStep downloadLink={this.state.downloadLink}/>;
        }

        const errorPanel = this.state.errorMessage !== '' && (
          <Alert bsStyle="danger">
              <h4>{this.state.errorMessage}</h4>
          </Alert>
        );

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
              <Row style={{marginTop: '10px'}}>
                  <Col md={12}>
                      {errorPanel}
                  </Col>
              </Row>
          </Grid>
        );
    }
}

export default App;
