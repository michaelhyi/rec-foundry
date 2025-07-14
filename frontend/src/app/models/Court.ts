import { Player } from "./Player";

export interface Court {
  id: string;
  gymId: string;
  courtNumber: string;
  createdAt: string;
  updatedAt: string;
  teamOne: Player[];
  teamTwo: Player[];
  queueSize: number;
}