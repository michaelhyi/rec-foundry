"use client";

import { useEffect, useState } from "react";
import Container from "../components/Container";
import { getGyms } from "../http/gyms";
import { Gym } from "../models/Gym";

export default function Gyms() {
    const [gyms, setGyms] = useState<Gym[]>([]);

    useEffect(() => {
        (async () => {
            const data = await getGyms();
            setGyms(data);
        })();
    }, []);

    return (
        <Container active="gyms">
            <div className="p-6 overflow-y-auto">
                <h2 className="text-2xl font-semibold mb-4">Available Gyms</h2>
                <div className="grid gap-4 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
                    {gyms.map((gym) => (
                        <a
                            key={gym.id}
                            className="border border-gray-200 rounded-lg shadow-sm p-4 hover:shadow-md transition duration-200 cursor-pointer"
                            href={`/gyms/${gym.id}`}
                        >
                            <h3 className="text-lg font-bold mb-1">{gym.name}</h3>
                            <p className="text-sm text-gray-600 mb-2">{gym.address}</p>
                            <p className="text-xs text-gray-400">Created: {new Date(gym.createdAt).toLocaleDateString()}</p>
                            <p className="text-xs text-gray-400">Updated: {new Date(gym.updatedAt).toLocaleDateString()}</p>
                        </a>
                    ))}
                </div>
            </div>
        </Container>
    );
}