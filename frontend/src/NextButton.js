import React from 'react';
import { Button, Glyphicon } from 'react-bootstrap';
import Spinner from './Spinner';

export default function NextButton({disabled, onClick, showSpinner}) {
    return (
      <Button
        bsStyle="success"
        bsSize="large"
        block
        disabled={disabled}
        onClick={onClick}
      >
          Next <Glyphicon glyph="arrow-right"/> {showSpinner && <Spinner/>}
      </Button>
    );
}
