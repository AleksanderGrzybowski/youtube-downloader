import React from 'react';
import { FormControl, FormGroup } from 'react-bootstrap';
import NextButton from './NextButton';

export default function LinkStep({requestInProgress, movieLink, onNext, onMovieLinkChange}) {
    const nextButton = (
      <NextButton
        disabled={requestInProgress || movieLink.trim() === ''}
        onClick={onNext}
        showSpinner={requestInProgress}
      />
    );

    return (
      <div>
          <h3>Step 1: paste YouTube download link here:</h3>
          <FormGroup controlId="formBasicText">
              <FormControl
                type="text"
                value={movieLink}
                placeholder="YouTube URL"
                onChange={e => onMovieLinkChange(e.target.value)}
              />
          </FormGroup>
          {nextButton}
      </div>
    )
}
