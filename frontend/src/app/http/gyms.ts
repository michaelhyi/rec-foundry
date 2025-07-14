import axios from "axios";

export async function getGyms() {
    const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/gyms`);
    return response.data;
}