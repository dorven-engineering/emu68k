extern crate bytes;

pub mod memory;
pub mod defs;
pub mod roms;

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {
        assert_eq!(2 + 2, 4);
    }
}
