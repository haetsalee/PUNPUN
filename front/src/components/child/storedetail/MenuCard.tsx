import React, { useState } from 'react';
import axios from 'axios';
import { DetailedHTMLProps, ImgHTMLAttributes } from 'react';

import styled from 'styled-components';
import BookingModal from './BookingModal';

interface MenuCardProps {
  key: number;
  id: number;
  title: string;
  image: string;
  price: number;
}

type Menu = {
  id: number;
  title: string;
  image: string;
  price: number;
};

interface MenuCardImageProps
  extends DetailedHTMLProps<
    ImgHTMLAttributes<HTMLImageElement>,
    HTMLImageElement
  > {
  image: string;
}

const MenuCardContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ffffff;
  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  padding: 16px;
  max-width: 300px;
  cursor: pointer;
`;

const MenuCardImage = styled.div<MenuCardImageProps>`
  width: 100%;
  border-radius: 4px;
  margin-bottom: 16px;
  background-image: url(${(props) => props.image})
  background-size: cover;
  background-position: center;
`;

const MenuCardTitle = styled.div`
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 8px;
`;

const MenuCardPrice = styled.div`
  font-size: 14px;
  color: #666666;
`;

const HeartButtonWrapper = styled.div`
  margin-left: 190px;
  background-color: transparent;
  border: none;
  font-size: 20px;
  cursor: pointer;
`;

const HeartButton = styled.div``;

const MenuCard: React.FC<MenuCardProps> = ({ id, title, image, price }) => {
  const [showModal, setShowModal] = useState(false);
  const [liked, setLiked] = useState(false);

  const bookingButton = () => {
    setShowModal(true);
  };

  const onClose = () => {
    setShowModal(false);
  };

  const toggleLike = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    setLiked(!liked);
    console.log('liked: ' + liked);

    const method = liked ? 'delete' : 'post';
    axios[method]('favors' + id)
      .then((response) => {
        console.log(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  return (
    <>
      <MenuCardContainer>
        <div onClick={bookingButton}>
          <MenuCardImage image={image}>
            <HeartButtonWrapper>
              <button onClick={toggleLike}>{liked ? '💖' : '🖤'}</button>
            </HeartButtonWrapper>
          </MenuCardImage>
          <div>
            <MenuCardTitle>{title}</MenuCardTitle>
            <MenuCardPrice>{price}원</MenuCardPrice>
          </div>
        </div>
      </MenuCardContainer>
      {showModal && (
        <BookingModal menu={{ id, title, image, price }} onClose={onClose} />
      )}
    </>
  );
};

export default MenuCard;