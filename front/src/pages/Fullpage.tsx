import React, { useState } from 'react';
import ReactPageScroller, { SectionContainer } from 'react-page-scroller';

import FirstComponent from '../components/main/FirstComponent';
import SecondComponent from '../components/main/SecondComponent';
import ThirdComponent from '../components/main/ThirdComponent';
import FourthComponent from '../components/main/FourthComponent';
import FifthComponent from '../components/main/FifthComponent';
import Header from '../components/ui/Header';
import FirstComponent1 from '../components/main/FirstComponent1';
import FirstComponent2 from '../components/main/FirstComponent2';
import FirstCarosel from '../components/main/FirstCarosel';
import styled from 'styled-components';

const Div = styled.div`
  @font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
  }
`;

const FullPage = () => {
  const onSelect = (item: string) => {
    // console.log(item);
  };

  return (
    <>
      <Header onSelect={onSelect} />
      <ReactPageScroller>
        <FirstCarosel />
        {/* <FirstComponent />
        <FirstComponent1 />
        <FirstComponent2 /> */}
        <SecondComponent />
        <ThirdComponent />
        {/* <SectionContainer height={50}></SectionContainer> */}
        {/* <FifthComponent /> */}
        <FourthComponent />
      </ReactPageScroller>
    </>
  );
};

export default FullPage;