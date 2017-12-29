import React from 'react';
import Spinner from './Spinner';

export default function WaitStep() {
    return (
      <div>
          <h1 className="text-center"><Spinner/></h1>
          <h3 className="text-center">Please wait...</h3>
      </div>
    )
}
