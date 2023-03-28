import React, { useEffect, useState } from 'react';
import axios, { AxiosInstance } from 'axios';
import Cookies from 'js-cookie';

const API_URL = 'https://j8d109.p.ssafy.io/';

function API(): AxiosInstance {
  const [accessToken, setAccessToken] = useState<string | undefined>(
    Cookies.get('access_token')
  );

  useEffect(() => {
    // Listen for changes to access_token cookie and update the state accordingly
    const handleAccessTokenChange = () => {
      setAccessToken(Cookies.get('access_token'));
      console.log('안녕');
    };
    window.addEventListener('access_token_change', handleAccessTokenChange);
    return () => {
      window.removeEventListener(
        'access_token_change',
        handleAccessTokenChange
      );
    };
  }, []);

  const apiClient = axios.create({
    baseURL: API_URL,
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
  });

  // Interceptor to refresh the access token if it has expired
  apiClient.interceptors.response.use(
    (response) => response,
    async (error) => {
      const originalRequest = error.config;
      if (error.response.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;
        try {
          const response = await apiClient.post('/api/auth/refresh', {
            refresh_token: Cookies.get('refresh_token'),
          });
          const newAccessToken = response.data.access_token;
          Cookies.set('access_token', newAccessToken);
          window.dispatchEvent(new Event('access_token_change'));
          return apiClient(originalRequest);
        } catch (error) {
          Cookies.remove('access_token');
          Cookies.remove('refresh_token');
          window.location.href = '/login';
        }
      }
      return Promise.reject(error);
    }
  );

  return apiClient;
}

export default API;
