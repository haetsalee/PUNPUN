import React from 'react';
import styled from 'styled-components';

const Input = styled.input`
  border: none;
  border-radius: 25px;
  background-color: hsl(0, 0%, 90%);
  padding: 10px;
  font-size: medium;
  width: 300px;
`;

type SearchBarProps = {
  value: string;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  onSubmit: (event: React.FormEvent<HTMLFormElement>) => void;
};

const SearchBar = ({ value, onChange, onSubmit }: SearchBarProps) => (
  <form onSubmit={onSubmit}>
    <Input
      type="text"
      value={value}
      onChange={onChange}
      placeholder="가게명을 입력해주세요."
    />
  </form>
);

export default SearchBar;
