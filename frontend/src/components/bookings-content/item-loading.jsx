import Skeleton from '@mui/material/Skeleton';
import Box from '@mui/material/Box';


const BookingItemLoading = () => (
    <>
        <Box className="booking-item">
            <div className="booking-content">
                <Skeleton width="100px" height="40px"/>
            </div>
            <div className="booking-tickets">
                <Skeleton variant="rectangular" width="100%" height="100%"/>
            </div>
            <div className="booking-action">
                <Skeleton variant="rectangular" width="80%" height="40%"/>
            </div>
        </Box>
    </>
);

export default BookingItemLoading