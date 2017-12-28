import React, { Component } from 'react';
import { Button, Checkbox, FormGroup, Glyphicon } from 'react-bootstrap';

export default class MediaTypeStep extends Component {

    render() {
        const requestInProgessIcon = this.props.requestInProgess && <Glyphicon glyph="time"/>;
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
              <img src={this.props.thumbnailLink} width={100} height={100}/>
              <h3>Step 2: pick media type:</h3>
              <FormGroup>
                  <Checkbox
                    value="video"
                    checked={this.props.mediaType === 'video'}
                    onChange={e => this.props.onMediaTypeChange(e.target.value)}
                  >
                      Video
                  </Checkbox>
                  <Checkbox
                    value="audio"
                    checked={this.props.mediaType === 'audio'}
                    onChange={e => this.props.onMediaTypeChange(e.target.value)}
                  >
                      Audio
                  </Checkbox>
              </FormGroup>
              {button}
          </div>
        )
    }
}
