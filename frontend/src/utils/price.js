import i18n from '../i18n'

export const getPrice = (price, starting = true) => {
    if (price > 0)
        return (starting ? i18n.t("event.starting") : "") + " $" + price;
    if (price == 0)
        return i18n.t("event.free")
    return i18n.t("event.noTickets")
}
