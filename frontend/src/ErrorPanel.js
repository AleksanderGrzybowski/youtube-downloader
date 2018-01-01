import React from 'react';
import { Alert } from 'react-bootstrap';

export default function ErrorPanel({message}) {
    // noinspection JSConstructorReturnsPrimitive

    return message === '' ? null : (
      <Alert bsStyle="danger">
          <h4>{message}</h4>
          {/* I'm not a frontend dev */}
          <h4>
              Try&nbsp;
              <span
                style={{cursor: 'pointer', fontWeight: 'bold', textDecoration: 'underline'}}
                onClick={() => window.location.reload()}
              >
                  refreshing 
              </span>
              &nbsp;the page.
          </h4>
      </Alert>
    );
}
