
export const getPrice = (price, freeMsg, noTicketsMsg) => {
    if (price > 0)
        return "$" + price;
    if (price == 0)
        return freeMsg
    return noTicketsMsg
}