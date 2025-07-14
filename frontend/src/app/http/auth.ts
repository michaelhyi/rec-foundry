import axios from "axios";

export async function login(email: string, password: string) {
  const response = await axios.post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/auth/login`, {
    email,
    password,
  });

  if (response.status !== 200) {
    throw new Error('Login failed');
  }

  return response.data;
}