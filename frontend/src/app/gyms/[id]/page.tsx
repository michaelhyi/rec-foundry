"use client";

import Container from "@/app/components/Container";
import { getGymById, joinQueueWithStrategy } from "@/app/http/gyms";
import { Gym } from "@/app/models/Gym";
import { IoIosRefresh } from "react-icons/io";
import { useCallback, useEffect, useState } from "react";
import { getNextTeam } from "@/app/http/courts";
import { FaUserPlus } from "react-icons/fa";
import { getPlayer } from "@/app/http/player";
import { Toaster } from "@/components/ui/sonner";
import { toast } from "sonner";

export default function GetGym({
    params
}: { params: Promise<{ id: string }> }) {
    const [gym, setGym] = useState<Gym | null>(null);
    const [strategy, setStrategy] = useState("SHORTEST_QUEUE");

    const handleSwapTeam = useCallback(async (courtId: string, teamId: number) => {
        await getNextTeam(courtId, teamId);
    }, []);

    const handleJoinQueue = useCallback(async () => {
        const { id: gymId } = await params;
        const userId = localStorage.getItem("userId");
        const playerId = (await getPlayer(userId!)).id;

        const { courtNumber } = await joinQueueWithStrategy(gymId, playerId, strategy);
        toast(`Joined queue for court number: ${courtNumber}`);
    }, [params, strategy]);

    useEffect(() => {
        (async () => {
            const { id } = await params;
            const data = await getGymById(id);
            setGym(data);
        })();
    }, [params]);

    if (!gym) {
        return <Container active="gyms">
            <div className="p-6">
                <h1 className="text-2xl font-bold mb-1">Loading...</h1>
            </div>
        </Container>;
    }

    return (
        <Container active="gyms">
            <Toaster />
            <div className="p-6">
                <h1 className="text-2xl font-bold mb-1">{gym.name}</h1>
                <p className="text-gray-600 mb-4">{gym.address}</p>

                <div className="flex items-center space-x-4 mb-6">
                    <select
                        className="border border-gray-300 rounded-lg px-4 py-2 text-sm"
                        value={strategy}
                        onChange={(e) => setStrategy(e.target.value)}
                    >
                        <option value="SHORTEST_QUEUE">Shortest Queue</option>
                        <option value="BALANCED_QUEUE">Most Balanced Skill</option>
                    </select>
                    <button
                        onClick={() => handleJoinQueue()}
                        className="flex items-center space-x-2 bg-black text-white text-sm font-medium px-4 py-2 rounded-lg hover:opacity-75 transition cursor-pointer"
                    >
                        <FaUserPlus />
                        <p>Join Queue</p>
                    </button>
                </div>

                <div className="space-y-6">
                    {gym.courts && gym.courts.map((court) => (
                        <div
                            key={court.id}
                            className="border border-gray-200 rounded-lg shadow-sm p-4"
                        >
                            <div className="flex items-center mb-3">
                                <h2 className="text-lg font-semibold">
                                    Court #{court.courtNumber}
                                </h2>
                                <div className="ml-auto flex items-center space-x-2">
                                    <button onClick={() => handleSwapTeam(court.id, 1)} className="flex items-center space-x-2 px-4 text-xs font-medium  bg-black text-white p-2 rounded-lg hover:opacity-50 cursor-pointer duration-200 transition">
                                        <IoIosRefresh />
                                        <p>Swap Team One</p>
                                    </button>
                                    <button onClick={() => handleSwapTeam(court.id, 2)} className="flex items-center space-x-2 px-4 text-xs font-medium  bg-white text-black border border-gray-300 p-2 rounded-lg hover:opacity-50 cursor-pointer duration-200 transition">
                                        <IoIosRefresh />
                                        <p>Swap Team Two</p>
                                    </button>
                                </div>
                            </div>

                            <a href={`/courts/${court.id}/queue`} className="text-sm text-gray-500 mb-3 underline hover:opacity-75">
                                Queue: {court.queueSize ?? 0} player{(court.queueSize ?? 0) !== 1 ? "s" : ""}
                            </a>

                            <div className="mt-3 grid grid-cols-2 gap-6">
                                <div>
                                    <h3 className="text-sm font-medium text-gray-700 mb-1">
                                        Team One
                                    </h3>
                                    <div className="grid grid-cols-5 gap-2">
                                        {[...Array(5)].map((_, i) => (
                                            <div
                                                key={i}
                                                className="h-16 flex items-center justify-center text-xs text-center border border-gray-300 rounded bg-gray-50"
                                            >
                                                {court.teamOne[i]
                                                    ? `${court.teamOne[i].firstName} ${court.teamOne[i].lastName}`
                                                    : "-"}
                                            </div>
                                        ))}
                                    </div>
                                </div>

                                <div>
                                    <h3 className="text-sm font-medium text-gray-700 mb-1">
                                        Team Two
                                    </h3>
                                    <div className="grid grid-cols-5 gap-2">
                                        {[...Array(5)].map((_, i) => (
                                            <div
                                                key={i}
                                                className="h-16 flex items-center justify-center text-xs text-center border border-gray-300 rounded bg-gray-50"
                                            >
                                                {court.teamTwo[i]
                                                    ? `${court.teamTwo[i].firstName} ${court.teamTwo[i].lastName}`
                                                    : "-"}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </Container>
    );

}