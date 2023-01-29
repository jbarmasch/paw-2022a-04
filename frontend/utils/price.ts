import i18n from './i18n'


export const getPrice = (price) => {
    
    if (price > 0)
        return "$" + price;
    if (price == 0)
        return i18n.t("event.price")
    return i18n.t("event.noTickets")
}