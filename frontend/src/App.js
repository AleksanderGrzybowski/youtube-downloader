import React, { Component } from 'react';
import { Alert, Col, Grid, Row } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import LinkStep from './LinkStep';
import MediaTypeStep from './MediaTypeStep';
import WaitStep from './WaitStep';
import DownloadStep from './DownloadStep';
import { createDownloadJob, getDownloadJob, getThumbnailLink } from './backend';

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

    fetchThumbnailLink = () => {
        this.setState({requestInProgress: true, errorMessage: ''});
        getThumbnailLink()
          .then(thumbnailLink => {
              this.setState({thumbnailLink, step: 2});
          })
          .catch(e => {
              this.setState({requestInProgress: false, errorMessage: 'Could not validate URL, is it ok?'});
              console.error(e);
          });
    };

    startPolling = () => {
        const timerId = setInterval(() => {
            getDownloadJob(this.state.jobId)
              .then(job => {
                  if (job.status === 'SUCCESS') {
                      clearInterval(timerId);
                      this.setState({step: 4, downloadLink: 'TODO build download link from job id'})
                  }
              })
              .catch(e => {
                  this.setState({requestInProgress: false, errorMessage: 'Could not validate URL, is it ok?'});
                  console.error(e);
              });
        }, 2000);
    };

    createJobAndStartPolling = () => {
        createDownloadJob(this.state.movieLink)
          .then(job => {
              this.setState({requestInProgress: true, jobId: job.id, step: 3})
              this.startPolling();
          })
          .catch(e => {
              this.setState({requestInProgress: false, errorMessage: 'Could not validate URL, is it ok?'});
              console.error(e);
          });
    };


    render() {
        let view;

        if (this.state.step === 1) {
            view = (
              <LinkStep
                movieLink={this.props.movieLink}
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
              />
            )
        } else if (this.state.step === 3) {
            view = <WaitStep/>;
        } else if (this.state.step === 4) {
            view = (
              <DownloadStep
                downloadLink={this.state.downloadLink}
              />
            )
        }

        const errorPanel = (this.state.errorMessage !== '') && (
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
              <Row>
                  {errorPanel}
              </Row>
          </Grid>
        );
    }
}

export default App;
