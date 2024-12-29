export interface IClient {
  id: number;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  address?: string | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
