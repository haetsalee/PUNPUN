import React, { useState } from 'react';
import styled from 'styled-components';
import MenuCard from './MenuCard';
import MenuCart from './MenuCart';

const MenuListContainer = styled.div`
  width: 70%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin: 20px auto;
  gap: 20px;
`;

type Menu = {
  id: number;
  title: string;
  image: string;
  price: number;
};

const data: Menu[] = [
  {
    id: 1,
    title: '메뉴1',
    image: 'https://sample.com/menu1.jpg',
    price: 10000,
  },
  {
    id: 2,
    title: '메뉴2',
    image: 'https://sample.com/menu2.jpg',
    price: 12000,
  },
  {
    id: 3,
    title: '메뉴3',
    image: 'https://sample.com/menu3.jpg',
    price: 15000,
  },
  {
    id: 4,
    title: '메뉴4',
    image: 'https://sample.com/menu4.jpg',
    price: 8000,
  },
  {
    id: 5,
    title: '메뉴5',
    image: 'https://sample.com/menu5.jpg',
    price: 11000,
  },
  {
    id: 6,
    title: '메뉴6',
    image: 'https://sample.com/menu6.jpg',
    price: 9000,
  },
];

const MenuList: React.FC = () => {
  const [cartItems, setCartItems] = useState<Menu[]>([]);

  const handleAddToCart = (menu: Menu) => {
    setCartItems([...cartItems, menu]);
  };

  //   const handleRemoveFromCart = (newCartItems: Menu[]) => {
  //     setCartItems(newCartItems);
  //   };

  return (
    <>
      <MenuListContainer>
        {data.map((data, index) => (
          <MenuCard
            key={index}
            id={data.id}
            title={data.title}
            image={data.image}
            price={data.price}
            onAddToCart={handleAddToCart}
          />
        ))}
        {cartItems.length > 0 && (
          <MenuCart cartItems={cartItems} onAddToCart={handleAddToCart} />
        )}
      </MenuListContainer>
    </>
  );
};

export default MenuList;
