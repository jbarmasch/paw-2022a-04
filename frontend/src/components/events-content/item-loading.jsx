import Skeleton from '@mui/material/Skeleton';
import Box from '@mui/material/Box';


const EventLoading = () => (
    <>
        <Box className="event-card">
            <div className="event-card-action">
                <Skeleton variant="rectangular" className="event-card-image"/>
                <Box width="100%">
                    <Skeleton/>
                    <Skeleton width="60%"/>
                </Box>
            </div>
        </Box>
    </>
);


export default EventLoading