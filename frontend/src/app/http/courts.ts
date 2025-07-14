import axios from "axios";

export async function getCourtQueue(courtId: string) {
    const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/courts/${courtId}/queue`);
    return response.data;
}

export async function joinCourtQueue(courtId: string, playerId: string) {
    const response = await axios.post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/courts/queue`, {courtId, playerId});
    return response.data;
}