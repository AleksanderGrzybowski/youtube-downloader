import React from 'react';
import { Button, ButtonGroup, Image } from 'react-bootstrap';
import NextButton from './NextButton';

export default function MediaTypeStep({requestInProgress, thumbnailLink, mediaType, onNext, onMediaTypeChange}) {
    const nextButton = (
      <NextButton
        disabled={requestInProgress}
        onClick={onNext}
        showSpinner={requestInProgress}
      />
    );

    return (
      <div>
          <Image className="center-block" src={thumbnailLink} style={{maxWidth: 500}}/>
          <h3>Step 2: pick what to download:</h3>
          <ButtonGroup justified style={{marginBottom: '10px'}}>
              <ButtonGroup>
                  <Button
                    bsStyle={mediaType === 'video' ? 'primary' : 'default'}
                    onClick={() => onMediaTypeChange('video')}
                  >
                      Video (mp4)
                  </Button>
              </ButtonGroup>
              <ButtonGroup>
                  <Button
                    bsStyle={mediaType === 'audio' ? 'primary' : 'default'}
                    onClick={() => onMediaTypeChange('audio')}
                  >
                      Just audio (mp3)
                  </Button>
              </ButtonGroup>
          </ButtonGroup>
          {nextButton}
      </div>
    )
}
