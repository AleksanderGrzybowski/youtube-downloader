import React, { Component } from 'react';
import { Button, FormControl, FormGroup, Glyphicon } from 'react-bootstrap';

export default class LinkStep extends Component {

    render() {
        const requestInProgessIcon = this.props.requestInProgress && <Glyphicon glyph="time"/>;
        const button = (
          <Button 
            bsStyle="success" 
            disabled={this.props.requestInProgress}
            onClick={this.props.onNext}
          >
              Next {requestInProgessIcon}
          </Button>
        );

        return (
          <div>
              <h3>Step 1: paste YouTube download link here:</h3>
              <FormGroup
                controlId="formBasicText"
              >
                  <FormControl
                    type="text"
                    value={this.props.movieLink}
                    placeholder="Enter text"
                    onChange={e => this.props.onMovieLinkChange(e.target.value)}
                  />
              </FormGroup>
              {button}
          </div>
        )
    }
}
