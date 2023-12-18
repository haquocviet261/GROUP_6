
function Card(ten){
    return(
       <div className="card">
            <img src={'/cho/1.jpg'} alt="Image" />
            <h2 className="card-title">{ten}</h2>
            <p className="card-text">I make Youtube videos and play video games</p>
       </div> 
    );
}
export default Card

