import React, { useState } from 'react';
import Swal from 'sweetalert2';
import styled from 'styled-components';
import API from '../../store/API';
import { useRecoilState } from 'recoil';
import { userInfoState } from '../../store/atoms';
import { useNavigate } from 'react-router';



const H2 = styled.h2`
  display: flex;
  justify-content: center;
  margin-top: 2rem;
  color: #363261;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-width: 300px;
  margin: 0 auto;
`;

const InputLabel = styled.label`
  font-size: 1rem;
`;

const Button = styled.button`
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  background-color: #5D5A88;
  color: white;
  font-size: 1rem;
  cursor: pointer;
  width: 96%;
  margin-top: 10px;
  margin-bottom: 50px;

  &:hover {
    background-color: #9795B5;
  }
`;

const StyledInput = styled.input`
  &::-webkit-outer-spin-button,
  &::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  height: 40px;
  padding: 10px;
  font-size: 18px;
  border-radius: 25px;
  border: 1px solid #bdbdbd;
  margin-right: 10px;
`;

const NumberForm = () => {
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [name, setName] = useState('');
  const [userInfo, setUserInfo] = useRecoilState(userInfoState);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // console.log(`전화번호: ${phoneNumber}`);
  
    // 이름 입력란이 비어있을 경우 defaultValue 사용
    const formattedName = name || userInfo.userName;
  
    // 유효성 검사
    if (phoneNumber.length !== 11) {
      setError('전화번호는 11자리로 입력해야 합니다.');
      await Swal.fire({
        icon: 'warning',
        text: '전화번호는 11자리로 입력해야 합니다.',
        width: '30%',
      });
      return;
    }
  
    const formattedPhoneNumber = String(phoneNumber);
  
    try {
      const response = await API.patch('users/member/update', {
        name: formattedName,
        phoneNumber: formattedPhoneNumber,
      });
      // console.log(response.data);
      setUserInfo((prevUserInfo) => ({
        ...prevUserInfo,
        userName: formattedName,
        userNumber: formattedPhoneNumber,
      }));
      Swal.fire(
        '가입이 완료 되었습니다!',
        'PUNPUN의 가족이 되신 것을 환영합니다 :)',
        'success'
      )
      navigate('/');
      // 서버 응답 데이터 처리
    } catch (error) {
      // console.log(error);
      // 에러 처리
    }
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPhoneNumber(event.target.value);
  };

  const handleChange2 = (event: React.ChangeEvent<HTMLInputElement>) => {
    setName(event.target.value);
  };

  return (
    <>
      <H2>추가 정보 입력</H2>
      <Form onSubmit={handleSubmit}>
        <InputLabel htmlFor="name-input">이름 입력</InputLabel>
        <StyledInput
          id="name-input"
          type="string"
          placeholder={userInfo.userName}
          defaultValue={name || userInfo.userName}
          onChange={handleChange2}
          required
        />
        <InputLabel htmlFor="phone-input">전화번호 입력</InputLabel>
        <StyledInput
          id="phone-input"
          type="number"
          placeholder="※ 숫자만 입력해 주세요."
          value={phoneNumber}
          onChange={handleChange}
          required
        />
        <Button type="submit">전송</Button>
      </Form>
    </>
  );
};

export default NumberForm;
