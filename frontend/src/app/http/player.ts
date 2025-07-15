import axios from "axios";
import { Player } from "../models/Player";

export async function getPlayer(userId: string): Promise<Player> {
    const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/users/${userId}/player`);
    return response.data;
}

export async function createPlayer(userId: string, playerData: any) {
    const response = await axios.post(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/players`, {
        userId,
        ...playerData
    });
    return response.data;
}