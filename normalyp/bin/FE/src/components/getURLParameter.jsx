import React from 'react';
import { useParams } from 'react-router-dom';

function URLParameter() {
    const { id } = useParams();
    return ({id});
}

export default URLParameter;