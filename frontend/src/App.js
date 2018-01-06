import React, { Component } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import LinkStep from './LinkStep';
import MediaTypeStep from './MediaTypeStep';
import WaitStep from './WaitStep';
import DownloadStep from './DownloadStep';
import { createDownloadJob, getDownloadJob, getDownloadLink, getThumbnailLink, healthcheck } from './backend/backend';
import 'font-awesome-webpack';
import ErrorPanel from './ErrorPanel';

class App extends Component {

    constructor(props) {
        super(props);

        this.state = {
            backendHealthy: true,
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

    componentDidMount() {
        healthcheck().catch(() => this.setState({backendHealthy: false}));
    }

    onMovieLinkChange = movieLink => this.setState({movieLink});

    onMediaTypeChange = mediaType => this.setState({mediaType});

    startRequest = () => this.setState({requestInProgress: true, errorMessage: ''});

    fetchThumbnailLink = () => {
        this.startRequest();

        getThumbnailLink(this.state.movieLink)
          .then(thumbnailLink => this.setState({requestInProgress: false, thumbnailLink, step: 2}))
          .catch(() => this.setState({requestInProgress: false, errorMessage: 'An error occured when checking URL. Is it correct?'}));
    };


    createJobAndStartPolling = () => {
        this.startRequest();

        createDownloadJob(this.state.movieLink, this.state.mediaType)
          .then(job => {
              this.setState({requestInProgress: true, jobId: job.id, step: 3});
              this.startPolling();
          })
          .catch(() => this.setState({requestInProgress: false, errorMessage: 'An error occured while downloading. Please try again.'}));
    };
    
    startPolling = () => {
        this.startRequest();

        const timerId = setInterval(() => {
            getDownloadJob(this.state.jobId)
              .then(job => {
                  if (job.status === 'SUCCESS') {
                      clearInterval(timerId);
                      this.setState({step: 4, requestInProgress: false, downloadLink: getDownloadLink(this.state.jobId)})
                  } else if (job.status === 'ERROR') {
                      clearInterval(timerId);
                      this.setState({requestInProgress: false, errorMessage: 'An error occured while downloading. Please try again.'});
                  }
              })
              .catch(() => {
                  clearInterval(timerId);
                  this.setState({requestInProgress: false, errorMessage: 'An error occured while downloading. Please try again.'});
              });
        }, 2000);
    };

    render() {
        let view;
        const {step} = this.state;
        
        if (!this.state.backendHealthy) {
            view = <ErrorPanel message="Server inaccessible. Please try again later."/>
        } else if (step === 1) {
            view = (
              <LinkStep
                movieLink={this.state.movieLink}
                onMovieLinkChange={this.onMovieLinkChange}
                requestInProgress={this.state.requestInProgress}
                onNext={this.fetchThumbnailLink}
              />
            )
        } else if (step === 2) {
            view = (
              <MediaTypeStep
                thumbnailLink={this.state.thumbnailLink}
                mediaType={this.state.mediaType}
                onMediaTypeChange={this.onMediaTypeChange}
                onNext={this.createJobAndStartPolling}
                requestInProgress={this.state.requestInProgress}
              />
            )
        } else if (step === 3) {
            view = <WaitStep/>;
        } else if (step === 4) {
            view = <DownloadStep downloadLink={this.state.downloadLink}/>;
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
              <Row style={{marginTop: '10px'}}>
                  <Col md={12}>
                      <ErrorPanel message={this.state.errorMessage}/>
                  </Col>
              </Row>
          </Grid>
        );
    }
}

export default App;
