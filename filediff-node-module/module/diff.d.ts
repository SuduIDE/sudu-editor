
interface DiffModule {
    foo() : Promise<string>
}

export function moduleFactory(text: string): Promise<DiffModule>
