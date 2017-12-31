import React from 'react';
import { Alert } from 'react-bootstrap';

export default function ErrorPanel({message}) {
    // noinspection JSConstructorReturnsPrimitive
    
    return message === '' ? null : (
      <Alert bsStyle="danger">
          <h4>{message}</h4>
      </Alert>
    );
}
