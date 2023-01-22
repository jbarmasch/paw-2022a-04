import list from "../components/products-content/list";

export type VotesType = {
    count: number;
    value: number;
}

export type PunctuationType = {
    countOpinions: number;
    punctuation: number;
    votes: VotesType[]
}

export type ReviewType = {
    name: string;
    avatar: string;
    description: string;
    punctuation: number;
}

export type ProductType = {
    id: string;
    name: string;
    thumb: string;
    price: string;
    count: number;
    color: string;
    size: string;
    images: string[];
    discount?: string;
    currentPrice: number;
    punctuation: PunctuationType;
    reviews: ReviewType[];
}

export type ProductTypeList = {
    id: string;
    name: string;
    minPrice: string;
    color: string;
    image: string;
    discount?: string;
    currentPrice?: string;
}

export type ProductStoreType = {
    id: string;
    name: string;
    thumb: string;
    price: number;
    count: number;
    color: string;
    size: string;
}

export type TicketType = {
    id: string;
    ticketName: string;
    price: string;
    qty: string;
    booked: string;
    maxPerUser: string;
    starting?: string;
    until?: string;
}

export type NewTicketType = {
    ticketId: string;
    ticketName: string;
    price: string;
    qty: string;
    booked: string;
    maxPerUser: string;
    starting?: string;
    until?: string;
}

export type TicketBookingType = {
    qty: string;
    user: string;
    event: string;
    ticket: NewTicketType;
}

export type BookingType = {
    code: string
    id: string;
    event: string;
    rating: string;
    confirmed: string;
    ticketBookings: TicketBookingType[];
    userId: string;
}
