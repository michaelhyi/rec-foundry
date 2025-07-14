import { Court } from "./Court";

export interface Gym {
    id: string;
    name: string;
    address: string;
    createdAt: string;
    updatedAt: string;
    courts?: Court[];
}