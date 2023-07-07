import Box from "@mui/material/Box";
import Skeleton from "@mui/material/Skeleton";

const MyEventLoading = () => (
    <>
        <Box className="event-card">
            <div className="event-card-action">
                <div className="event-card-container">
                    <Skeleton variant="rectangular" className="event-card-image"/>
                </div>
                <Box width="100%">
                    <Skeleton/>
                    <Skeleton width="60%"/>
                </Box>
            </div>
        </Box>
    </>
);

export default MyEventLoading