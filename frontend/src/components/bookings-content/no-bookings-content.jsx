import i18n from '../../i18n'

const NoBookingsContent = () => (
    <div className="no-content">
        {i18n.t("bookings.noBookings")}
    </div>
);

export default NoBookingsContent