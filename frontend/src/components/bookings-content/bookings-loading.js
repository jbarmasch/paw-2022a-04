import BookingItemLoading from './item-loading';
import Skeleton from '@mui/material/Skeleton';


const BookingLoading = () => {
    return (
        <section className="users-content products-content">
            <div className="products-content__intro bookings-intro">
                <Skeleton width="170px" height="60px"/>
                <Skeleton width="170px" height="60px"/>
            </div>

            <div>
                {[...Array(8)].map((e, i) => <BookingItemLoading key={i}/>)}
            </div>
        </section>
    );
};

export default BookingLoading