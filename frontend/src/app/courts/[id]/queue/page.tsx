"use client";

import Container from "@/app/components/Container";
import { getCourtQueue, joinCourtQueue } from "@/app/http/courts";
import { getPlayer } from "@/app/http/player";
import { Player } from "@/app/models/Player";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";

export default function Queue({
    params
}: { params: Promise<{ id: string }> }
) {
    const router = useRouter();
    const [players, setPlayers] = useState<Player[]>([]);

    const handleJoinQueue = useCallback(async () => {
        const { id } = await params;
        const player = await getPlayer(localStorage.getItem("userId") || "");
        await joinCourtQueue(id, player.id);
        router.refresh();
    }, [params, router]);

    useEffect(() => {
        (async () => {
            const { id } = await params;
            const response = await getCourtQueue(id);
            setPlayers(response);
        })();
    }, [params]);

    return (
        <Container active="gyms">
            <div className="p-6">
                <h1 className="text-2xl font-bold">Player Queue</h1>
                <button onClick={handleJoinQueue} className="flex items-center space-x-2 mt-4 px-4 font-medium  bg-black text-white p-2 rounded-lg hover:opacity-80 cursor-pointer duration-200 transition">
                    <FaPlus />
                    <p>Join Queue</p>
                </button>

                {players.length === 0 ? (
                    <p className="text-gray-500">No players in the queue.</p>
                ) : (
                    <div className="mt-4 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        {players.map((player) => (
                            <div
                                key={player.id}
                                className="border border-gray-200 rounded-lg p-4 shadow-sm hover:shadow-md transition"
                            >
                                <h2 className="text-lg font-semibold">
                                    {player.firstName && player.lastName
                                        ? `${player.firstName} ${player.lastName}`
                                        : "Anonymous Player"}
                                </h2>
                                <p className="text-sm text-gray-600 mb-2">{player.bio}</p>

                                <div className="text-sm text-gray-700 space-y-1">
                                    <p><strong>Position:</strong> {player.position}</p>
                                    <p><strong>Height:</strong> {player.height}</p>
                                    <p><strong>Weight:</strong> {player.weight}</p>
                                    <p><strong>Experience:</strong> {player.yearsOfExperience} year(s)</p>
                                    <p><strong>Date of Birth:</strong> {player.dateOfBirth}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </Container>
    );

}