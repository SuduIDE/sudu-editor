
interface DiffModule {
    terminateWorkers(): void;
    fib(n: string): Promise<number>;
}

export function moduleFactory(text: string, nThreads: number): Promise<DiffModule>
